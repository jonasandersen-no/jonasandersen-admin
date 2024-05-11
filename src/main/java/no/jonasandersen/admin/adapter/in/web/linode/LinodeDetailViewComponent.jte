@param no.jonasandersen.admin.adapter.in.web.linode.LinodeDetailViewComponent.LinodeDetailView linodeDetailView

!{var instance = linodeDetailView.instance();}
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
            <span class="material-symbols-outlined">memory</span>
            <span>${instance.prettyPrintSpecs()}</span>
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