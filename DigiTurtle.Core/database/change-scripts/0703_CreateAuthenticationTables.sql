PRINT '-- Creating table CW.Permission';

CREATE TABLE DigiTurtle.[CW].[Permission] (
	Name nvarchar(100),
	Level int
);

PRINT '-- Populating table CW.Permission';

INSERT INTO DigiTurtle.[CW].[Permission] (Name, Level) VALUES ('PublicData', 1);
INSERT INTO DigiTurtle.[CW].[Permission] (Name, Level) VALUES ('User.Read', 10);
INSERT INTO DigiTurtle.[CW].[Permission] (Name, Level) VALUES ('User.Write', 15);
INSERT INTO DigiTurtle.[CW].[Permission] (Name, Level) VALUES ('System.Write', 25);

PRINT '-- Creating table CW.AccessRole';

CREATE TABLE DigiTurtle.[CW].[AccessRole] (
	Name nvarchar(100),
	Level int,
	Password nvarchar(64),
	Salt nvarchar(32)
);

PRINT '-- Populating table CW.AccessRole';

INSERT INTO DigiTurtle.[CW].[AccessRole] (Name, Level, Password, Salt) VALUES (
	'Client', 15, 'a01656a6b3f0d6aa37f883b411c9a18496c57cb35b41df03fa44ce9c09558adc', 'c2e494ff91be927424d89b68f92505ad'
);
INSERT INTO DigiTurtle.[CW].[AccessRole] (Name, Level, Password, Salt) VALUES (
	'ProducerServer', 25, '7824da3b79f3eb7570082e9f2f4746062801cb1cb97e75c8e844f9da5e5842cf', '0b426e75c7a1732478b5a5f333da4aba'
);
INSERT INTO DigiTurtle.[CW].[AccessRole] (Name, Level, Password, Salt) VALUES (
	'BattleServer', 25, '095a89f699b4da3c2fb1eea6d35330a2c4d1b1fe26664f2b6397e6f693c8cf88', 'ef3245ecf098d09f3a8d48b6b479f983'
);

PRINT '-- Creating view CW.RolePermission';

GO

CREATE VIEW [CW].[RolePermission] AS (
	SELECT 
	Access.Name AS RoleName, Access.Password AS Password, Access.Salt AS Salt, Permission.Name AS PermissionName
	FROM DigiTurtle.[CW].[AccessRole] Access INNER JOIN DigiTurtle.[CW].[Permission] Permission ON Access.Level >= Permission.Level
);

GO