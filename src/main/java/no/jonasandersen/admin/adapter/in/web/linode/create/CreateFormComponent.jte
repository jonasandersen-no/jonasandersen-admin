@param no.jonasandersen.admin.adapter.in.web.linode.create.CreateFormComponent.CreateFromView view

<div class="container">
    <form method="post">
        <div class="mb-3">
            <label for="serverType">Choose Server Type</label>
            <select class="form-select" id="serverType" name="serverType">
                <option selected disabled>Select one...</option>
                @for(var serverType : view.serverTypes())
                    <option value="${serverType}">${serverType}</option>
                @endfor
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>