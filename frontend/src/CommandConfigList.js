import { HalResource } from './hal/HalResource.js'
import { CommandConfig } from './CommandConfig.js'

export class CommandConfigList extends HalResource {
  constructor(json) {
    super(json)

    this.items = []
    this.page = json.page || null

    if (json._embedded && json._embedded.commandConfigList) {
      this.items = json._embedded.commandConfigList.map((item) => CommandConfig.fromJson(item))
    }
  }

  static fromJson(json) {
    return new CommandConfigList(json)
  }
}
