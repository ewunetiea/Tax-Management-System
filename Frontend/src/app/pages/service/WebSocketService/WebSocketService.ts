import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment.prod';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { AuthService } from '../admin/auth.service';
import { StorageService } from '../admin/storage.service';
const baseUrl = environment.backendUrl;

@Injectable({ providedIn: 'root' })
export class WebSocketService {
    private stompClient?: Client;
    constructor(
        private authService: AuthService,
        private storageService: StorageService
    ) {}

    connect(): void {
        try {
            const socket = new SockJS(`${baseUrl}`);
            let username = this.storageService.getUser().email;
            this.stompClient = new Client({
                webSocketFactory: () => socket as any,
                reconnectDelay: 5000,
                onConnect: () => {
                    this.stompClient?.subscribe('/api/activeSessionExists', (message: any) => {
                        if (message.body === username) {
                            this.logout();
                        }
                    });
                },
                onStompError: (frame: any) => {},
                onWebSocketClose: () => {},

                onWebSocketError: (error: any) => {}
            });

            this.stompClient.activate();
        } catch (error) {}
    }

    logout(): void {
        this.authService.logout(this.storageService.getUser().id).subscribe({
            next: (res) => {
                this.storageService.clean();
                window.location.reload();
            },
            error: (err) => {}
        });
    }

    public disconnect(): void {
        if (this.stompClient?.active) {
            this.stompClient.deactivate();
        }
    }
}
