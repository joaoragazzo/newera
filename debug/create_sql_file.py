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

with open('database.sql', 'w') as sql_file:
    
    for user_id in range(1, 9999):
        karma = randint(0, 1000)
        steam64id = generate_steam64id()
        
        has_discord_id = True if randint(0, 100) > 60 else False

        if has_discord_id:
            discord_id = f"\"{randint(100000000000000000, 999999999999999999)}\""
        else:
            discord_id = "NULL"

        user_add_sql = f"INSERT INTO player (steam64id, discord_id, karma) VALUES (\"{steam64id}\", {discord_id}, {karma});\n"
        sql_file.write(user_add_sql)

        amount_of_nicks = randint(1, 3)

        for _ in range(0, amount_of_nicks):
            nickname = faker.first_name_male()
            formatted_datetime, _ = generate_random_localdatetime()
            nickname_add_sql = f"INSERT INTO nick (name, player_id, created_at) VALUES (\"{nickname}\", {user_id}, \"{formatted_datetime}\");\n"
            sql_file.write(nickname_add_sql)

        leader_of_clan = True if randint(0, 100) > 90 else False 

        if leader_of_clan:
            clan_id = len(CLANS) + 1
            clan_name = faker.cryptocurrency_name()
            clan_tag = faker.cryptocurrency_code()[:3]
            created_at, datetime_ = generate_random_localdatetime()
            color = faker.hex_color()

            clan_add_sql = f"INSERT INTO clan (name, tag, color, created_at) VALUES (\"{clan_name}\", \"{clan_tag}\", \"{color}\", \"{created_at}\");\n"
            sql_file.write(clan_add_sql)

            clan_filiation_sql = f"INSERT INTO clan_filiation (player_id, clan_id, role, joined_at) VALUES ({user_id}, {clan_id}, \"OWNER\", \"{created_at}\");\n"
            sql_file.write(clan_filiation_sql)
            CLANS.append(clan_id)
        
            for player in range(1, user_id):
                player_in_clan = True if randint(0, 100) > 98 else False

                if player_in_clan and player not in PLAYERS_ON_CLAN:
                    member_role = "ADMIN" if randint(0, 100) > 75 else "MEMBER"
                    joined_at, _ = generate_random_localdatetime(min_date=datetime_)
                    clan_filiation_sql = f"INSERT INTO clan_filiation (player_id, clan_id, role, joined_at) VALUES ({player}, {clan_id}, \"{member_role}\", \"{joined_at}\");\n"
                    sql_file.write(clan_filiation_sql)
                    PLAYERS_ON_CLAN.append(player)
