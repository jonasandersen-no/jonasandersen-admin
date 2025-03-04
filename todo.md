# Save Files

- Contains a list of save files that exists
- A save file is a one to one mapping towards a linode volume
- Save file should contain
    - Name (Display Name)
    - Owner
    - save file id (Similar to Linode's Volume Id)
    - If the save file is in use or not
        - Optional: LinodeId if its in use.
- Users can create save files that will create new volumes to store data.
- Already created save files that are unattached can be used to create a server to serve the data stored in the save
  file.
- Players can detach a save file to stop and destroy the server, but retain the data stored in the save file.