@param no.jonasandersen.admin.adapter.in.web.settings.UserSettingsViewComponent.UserSettingsView view

<div class="container">
    <h1>Settings</h1>

    <p>Currently logged in
        as ${org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName()}</p>

    <form method="post">
        <div class="form-check">
            <input class="form-check-input" type="radio" name="theme" value="light"
                   id="flexRadioDefault1"
                   checked="${"light".equals(view.theme())}">
            <label class="form-check-label" for="flexRadioDefault1">
                Light
            </label>
        </div>
        <div class="form-check">
            <input class="form-check-input" type="radio" name="theme" value="dark"
                   id="flexRadioDefault2"
                   checked="${"dark".equals(view.theme())}">
            <label class="form-check-label" for="flexRadioDefault2">
                Dark
            </label>
        </div>
        <button class="btn btn-primary">Save</button>
    </form>

</div>