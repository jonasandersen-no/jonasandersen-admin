export class HalLink {
  constructor(
    public href: string,
    public templated?: boolean,
  ) {}

  static fromJson(json: any): HalLink {
    return new HalLink(json.href, json.templated)
  }
}
