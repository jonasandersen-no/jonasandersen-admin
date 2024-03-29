@param String serverString


@template.layouts.main(title = "Minecraft", buildProperties = new org.springframework.boot.info.BuildProperties(new java.util.Properties()), content = @`
    <h2 class="text-center sans-bold">Minecraft Server</h2>

    <div class="" style="text-align: center;">
        <div id="minecraft-info-container" hx-trigger="my-event from:body delay:2s, load" hx-get="/minecraft">
            <img alt="Result loading..." class="htmx-indicator" width="50" src="/img/loading.svg"/>
        </div>
        <button hx-get="/minecraft" hx-target="#minecraft-info-container" class="btn">Refresh</button>
        <button class="btn" hx-post="/minecraft">Start server</button>
    </div>
`)


