@param no.jonasandersen.admin.core.domain.LinodeInstance instance

@template.layouts.main(title = "Details", content = @`

    <div class="container w-50">
        <div>
            <h1>${instance.prettyPrintLabel()}</h1>
            <p class="small"><strong>ID:</strong> ${instance.linodeId().id()}</p>
        </div>
        <div class="row g-3">
            <div class="d-flex align-items-center gap-2">
                <span class="material-symbols-outlined">wifi</span>
                <span>${instance.prettyPrintIp()}</span>
            </div>
            <div class="d-flex align-items-center gap-2">
                <span class="material-symbols-outlined">label</span>
                <span>${instance.prettyPrintTags()}</span>
            </div>
            <div class="d-flex align-items-center gap-2">
                <span class="material-symbols-outlined">update</span>
                <span>${instance.prettyPrintStatus()}</span>
            </div>
            <div class="d-flex align-items-center gap-2">
                <span class="material-symbols-outlined">save</span>
                <span>${instance.prettyPrintVolumeNames()}</span>
            </div>
        </div>

    </div>
`)
