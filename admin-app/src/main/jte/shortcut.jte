@param String project

@template.layouts.main(title = "Shortcut", content = @`
    <h2 class="text-center sans-bold">Shortcuts</h2>
    <h3 class="text-center sans-bold">Project: ${project}</h3>
    <div class="text-center" style="width: 50%; margin: auto;">
        <div class="flex flex-center text-center"
             hx-ext="ws"
             ws-connect="/ws/shortcut/${project}">
            <table

            >
                <thead>
                <tr>
                    <th>Shortcut</th>
                    <th>Description</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody id="table-body">
                <tr>
                    <td>test</td>
                </tr>
                </tbody>
                <%--                <tbody id="table-body" hx-get="/hx/shortcut/${project}" hx-swap="innerHTML" hx-trigger="load">--%>
                <%--                </tbody>--%>
            </table>
        </div>
        <div>
            <form hx-post="/hx/shortcut"
                  hx-trigger="submit"
                  hx-swap="none">
                <label>
                    <input type="text" name="project" placeholder="Project">
                </label>
                <label>
                    <input type="text" name="shortcut" placeholder="Shortcut">
                </label>
                <label>
                    <input type="text" name="description" placeholder="Description">
                </label>
                <button class="btn" type="submit">Add</button>
            </form>
        </div>
    </div>
`)


