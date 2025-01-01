import React, { useState, useEffect } from "react";
import { Button, Card, Col, Form, Row, Select, Typography, message } from "antd";
import axios from "axios";

const { Title, Paragraph } = Typography;


const Debug = () => {
    const [options, setOptions] = useState([]);
    const [player, setPlayer] = useState("USUÁRIO NÃO LOGADO");

    const getPlayer = async () => {
        try {
            const response = await axios.post("/api/debug/session")
            const data = response.data

            setPlayer(data.player);
        } catch (error) {
            message.error(error.response.data.error)
        }
    }


    const getOptions = async () => {
        try {
            const response = await axios.get("/api/player/list");
            const data = response.data;

            const formattedOptions = Object.entries(data).map(([player_id, name]) => ({
                value: player_id,
                label: name,
            }));

            setOptions(formattedOptions);
        } catch (error) {
            message.error(error.response.data.error);
        }
    };

    useEffect(() => {
        getOptions();
    }, []);

    useEffect(() => {
        getPlayer();
    }, [])

    const onFinish = async (values) => {
        try {
            await axios.post("/api/debug/setsteam64id", values)
            await getPlayer()
        } catch (error) {
            message.error(error.response.data.error)
        }
    };

    const createDefaultDb = async () => {
        try {
            await axios.get("/api/debug/defaultdb")
            await getOptions();
        } catch (error) {
            message.error(error.response.data.error)
        }
    }

    return (
        <>
            <Row gutter={18} style={{ marginBottom: "20px" }}>
                <Col flex={1}>
                    <Card title="Criar banco de dados">
                        <Button type="primary" size="large" disabled={options.length > 0} onClick={createDefaultDb}>Criar valores de testes</Button>
                    </Card>
                </Col>

                <Col flex={1}>
                    <Card title="Usuário logado atualmente">
                        <Paragraph>Você está logado como:</Paragraph>
                        <Title level={2} style={{ marginTop: "0px" }}>{player}</Title>
                    </Card>
                </Col>

                <Col flex={1}>
                    <Card title="Selecionar abstração de jogador">
                        <Form onFinish={onFinish} >
                            <Form.Item name="player" label="Jogador" rules={[{ required: true, message: "Você precisa selecionar um jogador" }]}>
                                <Select placeholder="Selecione um jogador" options={options}></Select>
                            </Form.Item>
                            <Button type="primary" htmlType="submit" >Definir</Button>
                        </Form>
                    </Card>
                </Col>
            </Row>



        </>
    );
}

export default Debug;