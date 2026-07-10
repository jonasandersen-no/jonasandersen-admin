import { HalLink } from '@/hal/HalLink'

export class Utils {
  fromJson(json): HalLink {
    return { href: json.href, templated: json.templated }
  }
}
