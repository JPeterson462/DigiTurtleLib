INSERT INTO DigiTurtle.[CW].[User] (Id, Username, Coins) VALUES ('67AFB64D-2436-4104-840B-8FEC389C6C41', 'JPeterson', 5000);
INSERT INTO DigiTurtle.[CW].[User] (Id, Username, Coins) VALUES (NEWID(), 'Admin', 0);
INSERT INTO DigiTurtle.[CW].[User] (Id, Username, Coins) VALUES ('F62D70B7-0A02-496C-8565-4976FF96FB48', 'Test', 5000);

INSERT INTO DigiTurtle.[CW].[Resources] (UserId) VALUES ('67AFB64D-2436-4104-840B-8FEC389C6C41');
INSERT INTO DigiTurtle.[CW].[Resources] (UserId) VALUES ('F62D70B7-0A02-496C-8565-4976FF96FB48');

INSERT INTO DigiTurtle.[CW].[WorldMapping] (MapId, TileId, MapTitle)
	VALUES ('E36681EC-41B1-4A09-BB4C-4525561B6659', '20088D01-8B85-41EE-98F7-1CC803E31990', 'Default');
INSERT INTO DigiTurtle.[CW].[Tile] (Id, OwnerId, Filename, Q, R)
	VALUES ('20088D01-8B85-41EE-98F7-1CC803E31990', '67AFB64D-2436-4104-840B-8FEC389C6C41', 'tile_0_0.json', 0, 0);
	
INSERT INTO DigiTurtle.[CW].[WorldMapping] (MapId, TileId, MapTitle)
	VALUES ('E36681EC-41B1-4A09-BB4C-4525561B6659', 'D3E961B1-E2C2-47DA-B396-8E4e8DADB0C8', 'Default');
INSERT INTO DigiTurtle.[CW].[Tile] (Id, OwnerId, Filename, Q, R)
	VALUES ('D3E961B1-E2C2-47DA-B396-8E4e8DADB0C8', 'F62D70B7-0A02-496C-8565-4976FF96FB48', 'tile_1_0.json', 1, 0);