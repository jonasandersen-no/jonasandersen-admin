import { HalLink } from '@/hal/HalLink'

export type HalTemplateProperty = {
  name: string
  type?: string
  required?: boolean
  value?: any
}

export type HalTemplate = {
  method: string
  contentType?: string
  properties: HalTemplateProperty[]
}

export class HalResource {
  links: Record<string, HalLink> = {}
  templates: Record<string, HalTemplate> = {}

  constructor(json: any) {
    if (json._links) {
      Object.entries(json._links).forEach(([rel, link]) => {
        const normalizedLink = Array.isArray(link) ? link[0] : link

        if (normalizedLink?.href) {
          this.links[rel] = HalLink.fromJson(normalizedLink)
        }
      })
    }

    if (json._templates) {
      Object.entries(json._templates).forEach(([rel, template]) => {
        const normalizedTemplate = template as any

        this.templates[rel] = {
          method: normalizedTemplate.method,
          contentType: normalizedTemplate.contentType,
          properties: normalizedTemplate.properties ?? [],
        }
      })
    }
  }

  getLink(rel: string): HalLink | undefined {
    return this.links[rel]
  }

  getTemplate(rel: string): HalTemplate | undefined {
    return this.templates[rel]
  }
}
