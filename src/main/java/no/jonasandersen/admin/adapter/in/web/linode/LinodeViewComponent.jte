@param no.jonasandersen.admin.adapter.in.web.linode.LinodeViewComponent.LinodeView linodeView

<h2 class="text-center sans-bold">Linode instances</h2>

<div class="container m-auto">
    <div class="container text-center">
        <a class="btn btn-primary" href="/linode/create">Create new instance</a>
        <button class="btn btn-secondary" hx-get="/hx/linode/instance/refresh"
                hx-target="#instance-row" hx-swap="innerHTML">Refresh
        </button>
        <form action="/linode/create/minecraft" method="post">
            <button type="submit" class="btn btn-secondary">
                Create default Minecraft Server
            </button>
        </form>

    </div>
    <div id="form-container"></div>
    <div class="container pt-1">
        <div class="row row-gap-2" id="instance-row">
            @for(var instance : linodeView.instances())
                ${linodeView.linodeViewComponent().renderCard(instance)}
            @endfor
        </div>
    </div>
</div>


