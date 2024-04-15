@param no.jonasandersen.admin.core.domain.LinodeInstance instance

@template.layouts.main(title = "Details", buildProperties = new org.springframework.boot.info.BuildProperties(new java.util.Properties()), content = @`

    <div class="container m-auto "
         style="width: 50rem;">


        <div>
            <h1>${instance.prettyPrintLabel()}</h1>
            <p class="small"><strong>ID:</strong> ${instance.linodeId().id()}</p>
        </div>
        <div class="d-flex">
            <div class="flex-grow-1">
                <div class="d-flex align-items-center gap-2">
                    <span class="material-symbols-outlined">wifi</span>
                    <span>${instance.ip()}</span>
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
            <div class="d-flex align-items-end">
                <button class="btn btn-primary">Details</button>
            </div>
        </div>

    </div>
`)
