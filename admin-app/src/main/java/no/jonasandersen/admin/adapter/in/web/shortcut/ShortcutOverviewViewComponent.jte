@param no.jonasandersen.admin.adapter.in.web.shortcut.ShortcutOverviewViewComponent.ShortcutOverviewView view

<div class="text-center">
    <h1>Projects</h1>
    <div class="tab m-auto flex flex-center w-fit">
        @for (String projectName : view.projectNames())
            <a class="tablinks" href="/shortcut/${projectName}">${projectName}</a>
        @endfor
    </div>
</div>

