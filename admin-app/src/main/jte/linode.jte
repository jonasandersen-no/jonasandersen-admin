@param String serverString


@template.layouts.main(title = "Minecraft", buildProperties = new org.springframework.boot.info.BuildProperties(new java.util.Properties()), content = @`
    <h2 class="text-center sans-bold">Minecraft Server</h2>

    <div class="" style="text-align: center;">
        <div id="minecraft-info-container" hx-trigger="my-event from:body delay:2s, load"
             hx-get="/hx/minecraft">
            <img alt="Result loading..." class="htmx-indicator" width="50" src="/img/loading.svg"/>
        </div>
        <button hx-get="/hx/minecraft" hx-target="#minecraft-info-container" class="btn">Refresh
        </button>
        <button class="btn" hx-post="/hx/minecraft">Start server</button>
    </div>


    <div class="m-auto flex flex-center w-fit">
        <h2>Volumes</h2>
        <div hx-get="/hx/linode/volumes" hx-trigger="load">

        </div>
    </div>


`)


