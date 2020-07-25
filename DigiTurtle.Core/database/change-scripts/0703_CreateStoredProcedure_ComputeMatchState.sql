CREATE PROCEDURE ComputeMatchState @MatchId UNIQUEIDENTIFIER
AS
BEGIN TRAN

DECLARE @NumUsers int;
DECLARE @NumPending int;
DECLARE @NumAccepted int;
DECLARE @NumDeclined int;

SELECT * FROM DigiTurtle.[CW].[MatchUser] WHERE MatchId = @MatchId;
SET @NumUsers = @@RowCount;

SELECT * FROM DigiTurtle.[CW].[MatchUser] WHERE MatchId = @MatchId AND UserState = 'Pending';
SET @NumPending = @@RowCount;

SELECT * FROM DigiTurtle.[CW].[MatchUser] WHERE MatchId = @MatchId AND UserState = 'Accepted';
SET @NumAccepted = @@RowCount;

SELECT * FROM DigiTurtle.[CW].[MatchUser] WHERE MatchId = @MatchId AND UserState = 'Declined';
SET @NumDeclined = @@RowCount;

DECLARE @ComputedStatus nvarchar(100);

IF @NumAccepted = @NumUsers
BEGIN
	SET @ComputedStatus = 'New';
END

IF @NumPending > 0
BEGIN
	SET @ComputedStatus = 'Pending';
END

IF @NumDeclined > 0
BEGIN
	SET @ComputedStatus = 'Cancelled';
END

UPDATE DigiTurtle.[CW].[Match] SET Status = @ComputedStatus WHERE Id = @MatchId;

COMMIT TRAN
GO