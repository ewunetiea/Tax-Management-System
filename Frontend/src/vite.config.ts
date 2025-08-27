// vite.config.ts
import { defineConfig } from 'vite';
import tsconfigPaths from 'vite-tsconfig-paths';

export default defineConfig({
    plugins: [tsconfigPaths()],
    define: {
        global: 'window'
    },
    optimizeDeps: {
        esbuildOptions: {
            define: {
                global: 'window'
            }
        }
    }
});
