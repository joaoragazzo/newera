import React, { useRef, useState } from "react";
import {  Table, Input, Button, Space } from "antd";
import { DeleteOutlined, EditOutlined, EyeOutlined, SearchOutlined, ShoppingCartOutlined } from "@ant-design/icons";
import { IoAddCircleOutline } from "react-icons/io5";
import AddItemModal from "./components/AddItemModal";

const ItemManager = () => {

    const [isAddModalVisible, setIsAddModalVisible] = useState(false);
    const [searchText, setSearchText] = useState("");
    const [searchedColumn, setSearchedColumn] = useState("");
    const [data, setData] = useState([]);
    const searchInput = useRef(null);


    const handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        setSearchText(selectedKeys[0]);
        setSearchedColumn(dataIndex);
    };

    const handleReset = (clearFilters) => {
        clearFilters();
        setSearchText("");
    };

    const getColumnSearchProps = (dataIndex) => ({
        filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
            <div style={{ padding: 8 }}>
                <Input
                    ref={searchInput}
                    placeholder={`Pesquisar pelo nome`}
                    value={selectedKeys[0]}
                    onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{ marginBottom: 8, display: "block" }}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
                        icon={<SearchOutlined />}
                        size="small"
                        style={{ width: 90 }}
                    >
                        Pesquisar
                    </Button>
                    <Button
                        onClick={() => clearFilters && handleReset(clearFilters)}
                        size="small"
                        style={{ width: 90 }}
                    >
                        Resetar
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: (filtered) => (
            <SearchOutlined style={{ color: filtered ? "#1890ff" : undefined }} />
        ),
        onFilter: (value, record) =>
            record[dataIndex]
                .toString()
                .toLowerCase()
                .includes(value.toLowerCase()),
        render: (text) =>
            searchedColumn === dataIndex ? (
                text
            ) : (
                text
            ),
    });

    const columns = [
        {
            title: "Nome",
            dataIndex: "item_name",
            key: "item_name",
            ...getColumnSearchProps("item_name"),
        },
        {
            title: "Categoria",
            dataIndex: "category",
            key: "category",
            filters: [
                { text: "Armas", value: "Armas" },
                { text: "Roupas", value: "Roupas" },
                { text: "Consumíveis", value: "Consumíveis" },
            ],
            onFilter: (value, record) => record.category === value,
        },
        {
            title: "Tipo",
            dataIndex: "type",
            key: "type",
            filters: [
                { text: "Permanente", value: "Permanente" },
                { text: "Temporário", value: "Temporário" },
                { text: "Consumível", value: "Consumível" },
            ],
            onFilter: (value, record) => record.type === value,
        },
        {
            title: "Tag",
            dataIndex: "tag",
            key: "tag",
        },
        {
            title: "Preço",
            dataIndex: "price",
            key: "price",
            sorter: (a, b) => a.price - b.price,
        },
        {
            title: "Ação",
            key: "action",
            width: "0",
            render: (_, record) => (
                <Space>
                    <Button type="dashed" icon={< ShoppingCartOutlined />} />
                    <Button type="dashed" icon={< EyeOutlined />} />
                    <Button type="dashed" icon={< EditOutlined />} />
                    <Button type="dashed" icon={< DeleteOutlined />} />
                </Space>
            )
        }
    ];

    return (
        <>
            <AddItemModal isAddModalVisible={isAddModalVisible} setIsAddModalVisible={setIsAddModalVisible} />
            <div style={{ marginBottom: "20px", textAlign: "right" }}>
                <Button icon={<IoAddCircleOutline />} size="large" onClick={() => { setIsAddModalVisible(true) }}>Adicionar novo item</Button>
            </div>
            <Table columns={columns} dataSource={data} />
        </>
    );
};

export default ItemManager;
