from faker import Faker
from datetime import datetime, timedelta
from random import randint

faker = Faker()

def generate_random_localdatetime(min_date=None):
    now = datetime.now()

    if min_date:
        if isinstance(min_date, str):
            min_date = datetime.strptime(min_date, '%Y-%m-%d %H:%M:%S.%f')
        delta = now - min_date
        total_seconds = delta.total_seconds()

        random_seconds = randint(0, int(total_seconds))
        datetime_ = now - timedelta(seconds=random_seconds)
    else:
        days_ago = randint(0, 30)
        seconds_ago = randint(0, 86400)
        datetime_ = now - timedelta(days=days_ago, seconds=seconds_ago)

    random_datetime = datetime_.strftime('%Y-%m-%d %H:%M:%S.%f')
    return random_datetime, datetime_


def generate_steam64id():
    initial = "7656119" + str(randint(1000000000, 9999999999))
    return initial 

CLANS = []
PLAYERS_ON_CLAN = []

players = []
nicks = []
clans = []
clan_filiations = []

for user_id in range(1, 9999):
    karma = randint(0, 1000)
    steam64id = generate_steam64id()
    
    has_discord_id = True if randint(0, 100) > 60 else False

    if has_discord_id:
        discord_id = f"{randint(100000000000000000, 999999999999999999)}"
    else:
        discord_id = None

    players.append(f"(\"{steam64id}\", {f'\"{discord_id}\"' if discord_id else 'NULL'}, {karma})")

    amount_of_nicks = randint(1, 3)

    for _ in range(0, amount_of_nicks):
        nickname = faker.first_name_male()
        formatted_datetime, _ = generate_random_localdatetime()
        nicks.append(f"(\"{nickname}\", {user_id}, \"{formatted_datetime}\")")

    leader_of_clan = True if randint(0, 100) > 90 else False 

    if leader_of_clan:
        clan_id = len(CLANS) + 1
        clan_name = faker.cryptocurrency_name()
        clan_tag = faker.cryptocurrency_code()[:3]
        created_at, datetime_ = generate_random_localdatetime()
        color = faker.hex_color()

        clans.append(f"(\"{clan_name}\", \"{clan_tag}\", \"{color}\", \"{created_at}\")")

        clan_filiations.append(f"({user_id}, {clan_id}, \"OWNER\", \"{created_at}\")")
        CLANS.append(clan_id)
    
        for player in range(1, user_id):
            player_in_clan = True if randint(0, 100) > 98 else False

            if player_in_clan and player not in PLAYERS_ON_CLAN:
                member_role = "ADMIN" if randint(0, 100) > 75 else "MEMBER"
                joined_at, _ = generate_random_localdatetime(min_date=datetime_)
                clan_filiations.append(f"({player}, {clan_id}, \"{member_role}\", \"{joined_at}\")")
                PLAYERS_ON_CLAN.append(player)

with open('database.sql', 'w') as sql_file:
    if players:
        sql_file.write(f"INSERT INTO player (steam64id, discord_id, karma) VALUES {', '.join(players)};\n")

    if nicks:
        sql_file.write(f"INSERT INTO nick (name, player_id, created_at) VALUES {', '.join(nicks)};\n")

    if clans:
        sql_file.write(f"INSERT INTO clan (name, tag, color, created_at) VALUES {', '.join(clans)};\n")

    if clan_filiations:
        sql_file.write(f"INSERT INTO clan_filiation (player_id, clan_id, role, joined_at) VALUES {', '.join(clan_filiations)};\n")


    _, randomDateTime = generate_random_localdatetime()
    sql_file.write(f"INSERT INTO category (name, created_at) VALUES (\"Armas\", \"{randomDateTime}\"), (\"Bases\", \"{randomDateTime}\"), (\"VIPs\", \"{randomDateTime}\")")