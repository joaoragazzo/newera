import { Button, Col, Form, Input, Space } from "antd";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";

const YoutubeLinks = () => {
    return (
        <Form.List name="youtubeLinks" >
            {(fields, { add, remove }) => (
                <>
                    <Col>
                        {fields.map(({ key, name, fieldKey, ...restField }) => (
                            <Space
                                key={key}
                                style={{ display: "flex", marginBottom: 8 }}
                                align="baseline"
                            >
                                <Form.Item
                                    {...restField}
                                    name={[name]}
                                    fieldKey={[fieldKey]}
                                    rules={[
                                        { required: true, message: "Por favor, insira o link do YouTube!" },
                                        { type: "url", message: "Insira um URL válido!" },
                                    ]}
                                >
                                    <Input placeholder="Insira o link do YouTube" />
                                </Form.Item>
                                <MinusCircleOutlined 
                                    onClick={() => remove(name)}
                                    style={{ color: "red" }}
                                />
                            </Space>
                        ))}
                        <Form.Item>
                            <Button
                                type="dashed"
                                onClick={() => add()}
                                block
                                icon={<PlusOutlined />}
                            >
                                Adicionar outro link
                            </Button>
                        </Form.Item>
                    </Col>
                </>

            )}
        </Form.List >
    );
}

export default YoutubeLinks;