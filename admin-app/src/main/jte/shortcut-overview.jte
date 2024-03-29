@param java.util.List<String> projectNames

@template.layouts.main(title = "Shortcut", buildProperties = new org.springframework.boot.info.BuildProperties(new java.util.Properties()), content = @`
    <div class="text-center">
        <h1>Projects</h1>
        <div class="tab m-auto flex flex-center w-fit">
            @for (String projectName : projectNames)
                <a class="tablinks" href="/shortcut/${projectName}">${projectName}</a>
            @endfor
        </div>
    </div>

`)