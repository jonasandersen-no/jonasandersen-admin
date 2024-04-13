@param no.jonasandersen.admin.core.domain.LinodeInstance instance


<div class="linode-instance container " style="width: 30rem;">

    <div class="">
        <h3 class="">${instance.prettyPrintLabel()}</h3>
        <p class="small"><strong>ID:</strong> ${instance.linodeId().id()}</p>
    </div>
    <div class="debug d-flex">
        <div>
            <div class="flex flex-row items-center gap-2">
                <span class="material-symbols-outlined">wifi</span>
                <span>${instance.ip()}</span>
            </div>
            <div class="flex flex-row items-center gap-2">
                <span class="material-symbols-outlined">label</span>
                <span>${instance.prettyPrintTags()}</span>
            </div>
            <div class="flex flex-row items-center gap-2">
                <span class="material-symbols-outlined">update</span>
                <span>${instance.prettyPrintStatus()}</span>
            </div>
            <div class="flex flex-row items-center gap-2">
                <span class="material-symbols-outlined">save</span>
                <span>${instance.prettyPrintVolumeNames()}</span>
            </div>
        </div>
        <div class="debug">
            <button class="btn btn-primary m-auto">Details</button>
        </div>
    </div>

</div>
