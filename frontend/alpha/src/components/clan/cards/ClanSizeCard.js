import { Statistic } from "antd"
import CountUp from 'react-countup';
import CardRoot from "../../general/card/CardRoot";

const ClanSizeCard = ({ users }) => {

    const formatter = (value) => {
        return (<CountUp end={value} separator="." />)
    }

    return (
        <CardRoot cardTitle={"Tamanho do clan"} cardToolTip={"Este card exibe a quantidade de usuários no clan."}>
            <div
                style={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                }}
            >
                <Statistic valueStyle={{ fontSize: "55px" }} value={users.length} formatter={formatter}></Statistic>

            </div>
        </CardRoot>
    );
}

export default ClanSizeCard;