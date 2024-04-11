@import static no.jonasandersen.admin.ThemeService.getTheme
@import org.springframework.boot.info.BuildProperties

@param BuildProperties buildProperties
@param gg.jte.Content content
@param String title


<html data-bs-theme="${getTheme()}" lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link href="/main.css" rel="stylesheet"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Open+Sans:ital,wght@0,300..800;1,300..800&display=swap"
            rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <script src="https://unpkg.com/htmx.org@1.9.11"></script>
    <script src="https://unpkg.com/htmx.org@1.9.11/dist/ext/ws.js"></script>
    <script src="https://unpkg.com/htmx.org@1.9.11/dist/ext/debug.js"></script>


    <style>
      nav {
        align-items: center;
        padding: 1rem;
        background-color: #f8f9fa;
        border-bottom: 1px solid #e9ecef;
        display: flex;
        justify-content: center;
      }

      footer {
        padding: 1rem;
        background-color: #f8f9fa;
        border-top: 1px solid #e9ecef;
        display: flex;
        justify-content: center;
      }

      nav ul {
        display: flex;
        list-style: none;
        margin: auto;
      }

      nav ul li {
        margin: 0 1rem;
      }

      nav ul li a {
        text-decoration: none;
        color: #007bff;
      }

      nav ul li a:hover {
        color: #0056b3;
      }

      html, body {
        margin: 0;
        height: 100%;
        min-height: 100%;
      }

      body {
        display: flex;
        flex-direction: column;
      }

      header,
      footer {
        flex: none;
      }

    </style>
    <title>${title}</title>
</head>
<body class="sans-regular" >
<header>
    <nav>
        <ul>
            <li><a href="/">Home</a></li>
            <li><a href="/linode">Linode</a></li>
            <li><a href="/shortcut">Shortcut</a></li>
        </ul>
    </nav>
</header>

<main hx-ext="debug">
    ${content}
</main>
<footer class="footer mt-auto">
    <p class="text-muted">Jonas Bjørge Andersen &copy; - Version <span>${buildProperties.getVersion()}</span></p>
</footer>

</body>
</html>
