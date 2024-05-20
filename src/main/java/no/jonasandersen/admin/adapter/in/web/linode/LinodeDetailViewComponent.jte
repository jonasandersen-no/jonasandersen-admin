@param no.jonasandersen.admin.adapter.in.web.linode.LinodeDetailViewComponent.LinodeDetailView view

<div class="container w-50">
    <div>
        <h1>${view.prettyPrintLabel()}</h1>
        <p class="small"><strong>ID:</strong> ${view.linodeId().id()}</p>
    </div>
    <div class="row g-3">
        <div class="d-flex align-items-center gap-2">
            <span class="material-symbols-outlined">person</span>
            <span>${view.owner()}</span>
        </div>
        <div class="d-flex align-items-center gap-2">
            <span class="material-symbols-outlined">wifi</span>
            <span>${view.prettyPrintIp()}</span>
        </div>
        <div class="d-flex align-items-center gap-2">
            <span class="material-symbols-outlined">memory</span>
            <span>${view.prettyPrintSpecs()}</span>
        </div>
        <div class="d-flex align-items-center gap-2">
            <span class="material-symbols-outlined">label</span>
            <span>${view.prettyPrintTags()}</span>
        </div>
        <div class="d-flex align-items-center gap-2">
            <span class="material-symbols-outlined">update</span>
            <span>${view.prettyPrintStatus()}</span>
        </div>
        <div class="d-flex align-items-center gap-2">
            <span class="material-symbols-outlined">save</span>
            <span>${view.prettyPrintVolumeNames()}</span>
        </div>
    </div>

</div>