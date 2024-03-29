@import org.springframework.boot.info.BuildProperties

@param BuildProperties buildProperties
@param gg.jte.Content content
@param String title

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link href="/main.css" rel="stylesheet"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Open+Sans:ital,wght@0,300..800;1,300..800&display=swap"
            rel="stylesheet">
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
