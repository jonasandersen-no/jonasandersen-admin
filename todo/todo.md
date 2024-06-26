# Minecraft server todo list

- [ ] Create UI for server status
- [ ] Create a controller serving the UI with info from the domain object.
    - [ ] If the service throws an exception related to the server not existing, the controller
      should return some kind of information to the UI.
- [ ] Create domain object representing server info
    - The following info is only available if the server is running.
        - [ ] Server name
        - [ ] Server status
        - [ ] Server IP
        - [ ] Server creation date
        - In the future it should also be possible to see who created the server.
- [ ] Create a server status service
    - [ ] It should be possible to get the current server status by calling a MinecraftFetcher.
- [ ] If a server is not running, throw an exception.
- [ ] Create MinecraftFetcher adapter interface
    - [ ] It should return a MinecraftInstance
    - [ ] Create a linode4j implementation of the MinecraftFetcher
    - The server info is fetched by checking if the linode4j service have a server running with
      the label `minecraft-auto-config-TIMESTAMP`

- [ ] Delete instance
    - [ ] Delete button in the UI
    - [ ] Controller method for deleting the instance
    - [ ] Delete calls shutdown script on the instance
    - [ ] Delete calls linode4j delete instance