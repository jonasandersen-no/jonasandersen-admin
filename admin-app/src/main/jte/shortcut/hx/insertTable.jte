@param no.jonasandersen.admin.core.shortcut.domain.Shortcut shortcut
<tbody id="target-body">
    <tr>
        <td>${shortcut.shortcut()}</td>
        <td>${shortcut.description()}</td>
        <td>
            <button
                    hx-get="/hx/shortcut/edit/${shortcut.id()}"
                    hx-target="#list-item-${shortcut.id()}"
                    hx-swap="outerHTML"
                    class="btn">
                Edit
            </button>
            <button
                    hx-delete="/hx/shortcut/${shortcut.id()}"
                    hx-target="#list-item-${shortcut.id()}"
                    hx-swap="outerHTML"
                    hx-confirm='Are you sure you want to delete this shortcut?'
                    class="btn">
                Delete
            </button>
        </td>
    </tr>
</tbody>