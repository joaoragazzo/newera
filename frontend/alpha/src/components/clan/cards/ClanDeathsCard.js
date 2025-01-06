import { Statistic } from "antd"
import CountUp from 'react-countup';
import CardRoot from "../../general/card/CardRoot";
import { useEffect, useState } from "react";

const ClanDeathsCard = ({ deaths }) => {

    const formatter = ( deaths ) => {
        return (<CountUp end={deaths} separator="." />)
    }

    return (
        <CardRoot cardTitle={"Mortes totais"}  cardToolTip={"Este card exibe a quantidade total de mortes do seu clan."}>
            <div
                style={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                }}
            >
                <Statistic valueStyle={{ fontSize: "55px" }} value={deaths} formatter={formatter}></Statistic>

            </div>
        </CardRoot>        
    );
}

export default ClanDeathsCard;