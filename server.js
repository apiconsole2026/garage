const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const APK_PATH = path.join(__dirname, 'RevisAuto_v3_Oficial.apk');

const server = http.createServer((req, res) => {
    // Serve the APK download
    if (req.url === '/download' || req.url === '/revisauto.apk' || req.url === '/app-debug.apk' || req.url === '/revisauto_v3_oficial.apk') {
        if (fs.existsSync(APK_PATH)) {
            const stat = fs.statSync(APK_PATH);
            res.writeHead(200, {
                'Content-Type': 'application/vnd.android.package-archive',
                'Content-Length': stat.size,
                'Content-Disposition': 'attachment; filename=RevisAuto_v3_Oficial.apk'
            });
            const readStream = fs.createReadStream(APK_PATH);
            readStream.pipe(res);
        } else {
            res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
            res.end('APK ainda não foi gerado ou está sendo compilado. Por favor, aguarde alguns instantes e recarregue a página.');
        }
        return;
    }

    // Serve the landing page
    if (req.url === '/' || req.url === '/index.html') {
        res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
        res.end(`
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Instalar RevisAuto</title>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary: #1B5E20;
            --primary-dark: #0D3C12;
            --primary-light: #E8F5E9;
            --accent: #2E7D32;
            --background: #F4F6F4;
            --surface: #FFFFFF;
            --text-main: #1C1D1C;
            --text-secondary: #5C5E5C;
            --border: #E0E2E0;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: 'Plus Jakarta Sans', sans-serif;
        }

        body {
            background-color: var(--background);
            color: var(--text-main);
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            padding: 24px;
        }

        .container {
            background: var(--surface);
            border-radius: 24px;
            box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
            border: 1px solid var(--border);
            max-width: 480px;
            width: 100%;
            padding: 40px 32px;
            text-align: center;
        }

        .logo-container {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 88px;
            height: 88px;
            background-color: var(--primary-light);
            border-radius: 22px;
            margin-bottom: 24px;
            box-shadow: 0 4px 12px rgba(27, 94, 32, 0.1);
        }

        .logo-icon {
            width: 48px;
            height: 48px;
            fill: var(--primary);
        }

        h1 {
            font-size: 28px;
            font-weight: 800;
            color: var(--primary-dark);
            margin-bottom: 8px;
            letter-spacing: -0.5px;
        }

        .subtitle {
            font-size: 15px;
            color: var(--text-secondary);
            margin-bottom: 32px;
            font-weight: 500;
        }

        .btn-download {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 12px;
            background-color: var(--primary);
            color: white;
            text-decoration: none;
            font-size: 16px;
            font-weight: 700;
            padding: 16px 32px;
            border-radius: 16px;
            width: 100%;
            transition: all 0.2s ease;
            box-shadow: 0 6px 20px rgba(27, 94, 32, 0.25);
            margin-bottom: 24px;
            cursor: pointer;
            border: none;
        }

        .btn-download:hover {
            background-color: var(--primary-dark);
            transform: translateY(-2px);
            box-shadow: 0 8px 24px rgba(27, 94, 32, 0.3);
        }

        .btn-download:active {
            transform: translateY(0);
        }

        .qr-section {
            background-color: var(--primary-light);
            padding: 24px;
            border-radius: 20px;
            margin-top: 8px;
            margin-bottom: 24px;
            display: flex;
            flex-direction: column;
            align-items: center;
            border: 1px dashed rgba(27, 94, 32, 0.2);
        }

        .qr-title {
            font-size: 14px;
            font-weight: 700;
            color: var(--primary-dark);
            margin-bottom: 12px;
        }

        .qr-code {
            width: 160px;
            height: 160px;
            background-color: white;
            padding: 8px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }

        .instructions {
            text-align: left;
            background: #FAFAFA;
            border-radius: 16px;
            padding: 20px;
            border: 1px solid var(--border);
        }

        .instructions-title {
            font-size: 14px;
            font-weight: 700;
            margin-bottom: 12px;
            color: var(--text-main);
        }

        .step {
            font-size: 13px;
            color: var(--text-secondary);
            margin-bottom: 8px;
            display: flex;
            gap: 10px;
            line-height: 1.5;
        }

        .step:last-child {
            margin-bottom: 0;
        }

        .step-num {
            background-color: var(--primary-light);
            color: var(--primary);
            font-weight: 700;
            width: 20px;
            height: 20px;
            border-radius: 50%;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
            font-size: 11px;
        }

        .footer-text {
            font-size: 11px;
            color: #9A9C9A;
            margin-top: 24px;
            font-weight: 500;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="logo-container">
            <svg class="logo-icon" viewBox="0 0 24 24">
                <path d="M12,2C6.48,2,2,6.48,2,12s4.48,10,10,10,10-4.48,10-10S17.52,2,12,2z M12,20c-4.41,0-8-3.59-8-8s3.59-8,8-8s8,3.59,8,8S16.41,20,12,20z M12,13c-1.1,0-2-0.9-2-2s0.9-2,2-2s2,0.9,2,2S13.1,13,12,13z M18,12c0,3.31-2.69,6-6,6s-6-2.69-6-6s2.69-6,6-6S18,8.69,18,12z"/>
            </svg>
        </div>
        <h1>Instalar RevisAuto</h1>
        <p class="subtitle">Controle de revisões preventivas & economia financeira</p>

        <a href="/download?v=3.0" class="btn-download">
            <svg style="width: 24px; height: 24px; fill: currentColor;" viewBox="0 0 24 24">
                <path d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM17 13l-5 5-5-5h3V9h4v4h3z"/>
            </svg>
            Baixar Aplicativo (APK)
        </a>

        <div class="qr-section">
            <div class="qr-title">Aponte a câmera para instalar no celular</div>
            <img class="qr-code" id="qrCodeImg" src="" alt="Carregando QR Code...">
        </div>

        <div class="instructions">
            <div class="instructions-title">Como Instalar:</div>
            <div class="step">
                <span class="step-num">1</span>
                <span>Toque no botão acima para baixar o arquivo <strong>RevisAuto_v3_Oficial.apk</strong>.</span>
            </div>
            <div class="step">
                <span class="step-num">2</span>
                <span>Ao concluir, abra o arquivo baixado nas notificações.</span>
            </div>
            <div class="step">
                <span class="step-num">3</span>
                <span>Se solicitado, permita a instalação de "Fontes Desconhecidas" no seu Android.</span>
            </div>
        </div>

        <p class="footer-text">RevisAuto • Desenvolvido de Forma Nativa em Kotlin & Compose</p>
    </div>

    <script>
        const downloadUrl = window.location.origin + '/download?v=3.0';
        const qrImage = document.getElementById('qrCodeImg');
        qrImage.src = 'https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=' + encodeURIComponent(downloadUrl);
    </script>
</body>
</html>
        `);
        return;
    }

    res.writeHead(404, { 'Content-Type': 'text/plain' });
    res.end('Not Found');
});

server.listen(PORT, '0.0.0.0', () => {
    console.log('Servidor de download rodando com sucesso na porta ' + PORT + '!');
});
