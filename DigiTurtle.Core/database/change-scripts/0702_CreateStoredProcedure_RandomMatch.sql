CREATE PROCEDURE RandomMatch @Teams int, @UsersPerTeam int
AS
BEGIN TRAN

DECLARE @NumUsers int;
SET @NumUsers = @Teams * @UsersPerTeam;

DECLARE @TargetState nvarchar(100);
SET @TargetState = 'Random_' + CAST(@Teams AS varchar(16)) + '_' + CAST(@UsersPerTeam as varchar(16));

SELECT TOP (@NumUsers) * FROM DigiTurtle.[CW].[MatchLobby] WHERE State = @TargetState;
IF @@ROWCOUNT = @NumUsers
BEGIN
	DECLARE @MatchId UNIQUEIDENTIFIER;
	SET @MatchId = NEWID();
	
	INSERT INTO DigiTurtle.[CW].[Match] (Id, Time, Type, Status, Filename) VALUES (@MatchId, GETDATE(), 'OnDemand', 'Pending', NULL);
	
	DECLARE @TeamId int;
	SET @TeamId = 1;

	DECLARE @DefaultUserStatus nvarchar(100);
	SET @DefaultUserStatus = 'Pending';
	
	DECLARE @UsersInTeam TABLE(MatchId UNIQUEIDENTIFIER, UserId UNIQUEIDENTIFIER, Team int, UserStatus nvarchar(100));
	
	WHILE @TeamId <= @Teams
	BEGIN
		DELETE FROM @UsersInTeam;
		INSERT INTO @UsersInTeam(MatchId, UserId, Team, UserStatus) (
			SELECT TOP (@UsersPerTeam) @MatchId AS MatchId, UserId, @TeamId as Team, @DefaultUserStatus as UserStatus FROM (
				SELECT UserId, State FROM (
					SELECT TOP (@NumUsers) UserId, State FROM DigiTurtle.[CW].[MatchLobby] ORDER BY NEWID()
				) AS UsersOnTeam WHERE State = @TargetState
			) AS UsersInLobby
		);
		INSERT INTO DigiTurtle.[CW].[MatchUser] (MatchId, UserId, Team, UserState) (SELECT * FROM @UsersInTeam);
		UPDATE DigiTurtle.[CW].[MatchLobby] SET State = 'Matched' WHERE UserId IN (SELECT UserId FROM @UsersInTeam);

		SET @TeamId = @TeamId + 1;
	END
	
END

COMMIT TRAN
GO;