import { Statistic } from "antd"
import CountUp from 'react-countup';
import CardRoot from "../../general/card/CardRoot";

const ClanKillsCard = ({ kills }) => {

    const formatter = (kills) => {
        return (<CountUp end={kills} separator="." />)
    }

    return (
        <CardRoot cardTitle={"Kills totais"}  cardToolTip={"Este card exibe a quantidade total de kills do seu clan."}>
            <div
                style={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                }}
            >
                <Statistic valueStyle={{ fontSize: "55px" }} value={kills} formatter={formatter}></Statistic>

            </div>
        </CardRoot>        
    );
}

export default ClanKillsCard;