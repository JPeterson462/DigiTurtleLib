-----------------------------------------------------------
-- Create the Resources tables
--   CW.Resources (UserId,
--					BatteryBasic, BatteryHighCapacity,
--					WeaponryAmmunition, WeaponryArmorPlating,
--					TechnologySpeed1, ...,
--					TechnologyFiringRate1, ...,
--					TechnologyAccuracy1, ...,
--					BotInfantry, BotArmored, BotCommander, BotSpecialOps)
-----------------------------------------------------------

PRINT '-- Creating table CW.Resources';

CREATE TABLE DigiTurtle.[CW].[Resources] (
	UserId UNIQUEIDENTIFIER,
	BatteryBasic int default 0,
	BatteryHighCapacity int default 0,
	WeaponryAmmunition int default 0,
	WeaponryArmorPlating int default 0,
	TechnologySpeed1 int default 0,
	TechnologyFiringRate1 int default 0,
	TechnologyAccuracy1 int default 0,
	BotInfantry int default 0,
	BotArmored int default 0,
	BotCommander int default 0,
	BotSpecialOps int default 0,
);