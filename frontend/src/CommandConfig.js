import { HalResource } from './hal/HalResource.js'

export class CommandConfig extends HalResource {
  constructor(json) {
    super(json)

    this.apiVersion = json.apiVersion
    this.kind = json.kind
    this.metadata = json.metadata
    this.name = json.name
    this.spec = json.spec
    this.type = json.type
  }

  static fromJson(json) {
    return new CommandConfig(json)
  }
}
