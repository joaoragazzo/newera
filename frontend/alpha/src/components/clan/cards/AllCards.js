import CardsRow from "../../general/card/CardsRow";
import ActiveMembersCard from "./ActiveMembersCard";
import ActivityMembersCard from "./ActivityMembersCard";
import ClanDeathsCard from "./ClanDeathsCard";
import ClanKillsCard from "./ClanKillsCard";
import ClanSizeCard from "./ClanSizeCard";


const AllCards = ({ users }) => {
    return (
        <div
            style={{
                overflowX: "auto",
                whiteSpace: "nowrap",
            }}
        >
            <CardsRow>
                <ClanKillsCard />
                <ClanDeathsCard />
                <ClanSizeCard users={users} />
                <ActiveMembersCard />
                <ActivityMembersCard />
            </CardsRow>
        </div>
    );
}

export default AllCards;