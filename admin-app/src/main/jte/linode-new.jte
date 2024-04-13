@param java.util.List<no.jonasandersen.admin.core.domain.LinodeInstance> instances


@template.layouts.main(title = "Minecraft", buildProperties = new org.springframework.boot.info.BuildProperties(new java.util.Properties()), content = @`
    <h2 class="text-center sans-bold">Linode instances</h2>

    <div class="container m-auto">
        <div class="container text-center">
            <button class="btn btn-primary" hx-post="/hx/linode/instance/create" hx-target="#instance-row" hx-swap="beforeend">Create new instance</button>
            <button class="btn btn-secondary" hx-get="/hx/linode/instance/refresh" hx-target="#instance-row" hx-swap="innerHTML">Refresh</button>
        </div>
        <div class="container pt-1">
            <div class="row row-gap-2" id="instance-row">
                @for(var instance : instances)
                    @template.linode.instance(instance = instance)
                @endfor
            </div>
        </div>
    </div>
`)


