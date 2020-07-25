-----------------------------------------------------------
-- Create the schema and tables
--   CW.User (Id, Username, Coins)
--   CW.Tile (Id, OwnerId, Filename, Q, R)
--   CW.WorldMapping (MapId, TileId)
-----------------------------------------------------------
IF EXISTS (SELECT name FROM sys.schemas WHERE name = N'CW')
	BEGIN
		PRINT '-- Removing existing schema CW'
		DROP SCHEMA [CW]
	END
GO

PRINT '-- Creating schema CW'
GO

CREATE SCHEMA [CW] AUTHORIZATION [dbo]
GO

IF EXISTS (SELECT name from sys.tables WHERE name = 'User')
	BEGIN
		PRINT '-- Removing existing table CW.User'
		DROP TABLE DigiTurtle.[CW].[User]
	END
GO

PRINT '-- Creating table CW.User';

CREATE TABLE DigiTurtle.[CW].[User] (
	Id UNIQUEIDENTIFIER PRIMARY KEY default NEWID(),
	Username nvarchar(100),
	Coins int default 0
);

IF EXISTS (SELECT name from sys.tables WHERE name = 'Tile')
	BEGIN
		PRINT '-- Removing existing table CW.Tile'
		DROP TABLE DigiTurtle.[CW].[Tile]
	END
GO

PRINT '-- Creating table CW.Tile';

CREATE TABLE DigiTurtle.[CW].[Tile] (
	Id UNIQUEIDENTIFIER PRIMARY KEY default NEWID(),
	OwnerId UNIQUEIDENTIFIER,
	Filename nvarchar(100), 
	Q int,
	R int
);

IF EXISTS (SELECT name from sys.tables WHERE name = 'WorldMapping')
	BEGIN
		PRINT '-- Removing existing table CW.WorldMapping'
		DROP TABLE DigiTurtle.[CW].[WorldMapping]
	END
GO

PRINT '-- Creating table CW.WorldMapping';

CREATE TABLE DigiTurtle.[CW].[WorldMapping] (
	MapTitle nvarchar(100),
	MapId UNIQUEIDENTIFIER,
	TileId UNIQUEIDENTIFIER,
	PRIMARY KEY (MapId, TileId)
);

PRINT '-- Creating table CW.AccessRole';

CREATE TABLE DigiTurtle.[CW].[AccessRole] (
	Name nvarchar(100),
	Level int,
	Password nvarchar(64),
	Salt nvarchar(32)
);

PRINT '-- Creating table CW.Permission';

CREATE TABLE DigiTurtle.[CW].[Permission] (
	Name nvarchar(100),
	Level int
);

GO