@param no.jonasandersen.admin.adapter.in.web.linode.create.CreateFormComponent.CreateFromView view

<div class="container">
    <form method="post">
        <div class="mb-3">
            <label for="instanceName" class="form-label">Instance Name</label>
            <input type="text" class="form-control" id="instanceName" name="instanceName">
        </div>
        <div class="mb-3">
            <label>Attach Volume</label>
            <select class="form-select" aria-label="Default select example" name="volumeId">
                <option selected disabled>Select one...</option>
                @for(var volume : view.volumes())
                    <option value="${volume.id()}">${volume.name()}</option>
                @endfor
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>