import React, { useState, useEffect } from "react";
import { Button, Card, Form, Select } from "antd";
import FormItem from "antd/es/form/FormItem";
import axios from "axios";




const Debug = () => {
    const [options, setOptions] = useState([]);

    const getOptions = async () => {
        try {
            const response = await axios.get("/api/debug/allplayers");
            const data = response.data;

            const formattedOptions = Object.entries(data).map(([player_id, name]) => ({
                value: player_id,
                label: name,
            }));

            setOptions(formattedOptions);
        } catch (error) {
            console.error("Erro ao buscar jogadores:", error);
        }
    };

    useEffect(() => {
        getOptions(); 
    }, []);

    const onFinish = (values) => {
        axios.post("/api/debug/setsteam64id", values)
            .then((response) => {
                console.log("Sucesso:", response.data);
            })
            .catch((error) => {
                console.error("Erro:", error);
            });
    };

    return (    
        <Card>
            <Form onFinish={onFinish} >
                <Form.Item name="player" label="Jogador" rules={[{ required: true, message: "You must select any player" }]}>
                    <Select placeholder="Select a player" options={options}></Select>
                </Form.Item>
                <Button type="primary" htmlType="submit" >Definir</Button>
            </Form>
        </Card>

    );
}

export default Debug;