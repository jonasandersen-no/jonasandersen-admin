@import no.jonasandersen.admin.adapter.in.web.layout.MainLayoutViewComponent.MainLayoutView

@param MainLayoutView mainLayoutView

<html data-bs-theme="${mainLayoutView.theme()}" lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Open+Sans:ital,wght@0,300..800;1,300..800&display=swap"
            rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined"
          rel="stylesheet"/>

    <link href="/main.css" rel="stylesheet"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <script src="https://unpkg.com/htmx.org@1.9.11"></script>
    <script src="https://unpkg.com/htmx.org@1.9.11/dist/ext/ws.js"></script>
    <script src="https://unpkg.com/htmx.org@1.9.11/dist/ext/debug.js"></script>
    <title>${mainLayoutView.title()}</title>
</head>
<body class="sans-regular">
<header>
    <nav class="navbar navbar-expand-lg bg-body-tertiary">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">Admin Panel</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
                    aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/linode">Linode</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/shortcut">Shortcut</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/settings">Settings</a>
                    </li>
                </ul>

            </div>
        </div>
    </nav>
</header>

<main hx-ext="debug">
    ${mainLayoutView.mainContent()}
</main>

<footer class="footer fixed-bottom">
    <div class=" d-flex justify-content-center">
        <p class="text-muted">Jonas Bjørge Andersen &copy; - Version
            <span>${mainLayoutView.buildVersion()}</span></p>
    </div>

</footer>

</body>
</html>
