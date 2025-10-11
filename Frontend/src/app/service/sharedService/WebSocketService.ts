import { Injectable, OnDestroy } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';

@Injectable({
    providedIn: 'root'
})
export class WebSocketService implements OnDestroy {
    private stompClient!: Client;
    private isConnected$ = new BehaviorSubject<boolean>(false);
    private connectionAttempts = 0;
    private readonly MAX_RETRIES = 3;
    private readonly RECONNECT_DELAY = 5000;

    constructor() {
        this.initializeWebSocket();
    }

    private initializeWebSocket(): void {
        try {
            this.stompClient = new Client({
                brokerURL: `${environment.backendUrl.replace(/^http/, 'ws')}/ws`,
                webSocketFactory: () => new SockJS(`${environment.backendUrl}/ws`),
                reconnectDelay: this.RECONNECT_DELAY,
                heartbeatIncoming: 10000,
                heartbeatOutgoing: 10000,

                // Disable debug in production
                debug: (msg) => {
                    if (!environment.production) {
                        console.debug('[WebSocket]', msg);
                    }
                },

                onConnect: () => {
                    this.connectionAttempts = 0;

                    this.isConnected$.next(true);
                },

                onStompError: (frame) => {
                    this.handleError(`STOMP protocol error: ${frame.headers['message']}`);
                    this.isConnected$.next(false);
                },

                onWebSocketError: (error) => {
                    this.handleError(`WebSocket error: ${error}`);
                    this.isConnected$.next(false);
                },

                onDisconnect: () => {
                    this.isConnected$.next(false);
                }
            });
        } catch (e) {
            this.handleError(`Initialization failed: ${e}`);
        }
    }

    private handleError(message: string): void {
        if (!environment.production) {
            console.error(`[WebSocket Error] ${message}`);
        }
        // In production, consider sending to error monitoring service
    }

    public connect(): Promise<boolean> {
        return new Promise((resolve) => {

            if (this.isConnected$.value) {
                resolve(true);
                return;
            }

            if (this.connectionAttempts >= this.MAX_RETRIES) {
                this.handleError('Max connection attempts reached');
                resolve(false);
                return;
            }

            this.connectionAttempts++;

            const connectionTimeout = setTimeout(() => {
                this.handleError('Connection timeout');
                resolve(false);
            }, 10000);

            this.stompClient.onConnect = () => {
                clearTimeout(connectionTimeout);
                resolve(true);
            };

            this.stompClient.onWebSocketError = (error) => {
                clearTimeout(connectionTimeout);
                this.handleError(`Connection error: ${error}`);
                resolve(false);
            };

            try {
                this.stompClient.activate();
            } catch (e) {
                clearTimeout(connectionTimeout);
                this.handleError(`Activation failed: ${e}`);
                resolve(false);
            }
        });
    }

    public connectionState$(): Observable<boolean> {
        return this.isConnected$.asObservable();
    }

    public onLogout$(): Observable<string> {
        return new Observable<string>((subscriber) => {
            if (!this.stompClient?.active) {
                subscriber.error('WebSocket not connected');
                return;
            }

            const sub = this.stompClient.subscribe('/user/queue/logout', (message: IMessage) => {
                try {

                    subscriber.next(message.body);
                } catch (e) {
                    this.handleError(`Error processing logout message: ${e}`);
                }
            });

            return () => {
                try {
                    sub.unsubscribe();
                } catch (e) {
                    this.handleError(`Error unsubscribing: ${e}`);
                }
            };
        });
    }

    public sendMessage(destination: string, body: any): void {
        if (!this.isConnected$.value) {
            this.handleError(`Attempted to send message while disconnected: ${destination}`);
            return;
        }

        try {
            this.stompClient.publish({
                destination,
                body: JSON.stringify(body),
                headers: { 'content-type': 'application/json' }
            });
        } catch (e) {
            this.handleError(`Failed to send message: ${e}`);
        }
    }

    public disconnect(): void {
        try {
            if (this.stompClient?.active) {
                this.stompClient.deactivate();
            }
        } catch (e) {
            this.handleError(`Error during disconnect: ${e}`);
        } finally {
            this.isConnected$.next(false);
        }
    }

    ngOnDestroy(): void {
        this.disconnect();
    }
}
