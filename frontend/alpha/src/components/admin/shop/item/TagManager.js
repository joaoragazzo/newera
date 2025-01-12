import { Button, Space, Input, Table } from "antd";
import { DeleteOutlined, EditOutlined, EyeOutlined, SearchOutlined } from "@ant-design/icons";
import { useState, useRef } from "react";
import { IoAddCircleOutline } from "react-icons/io5";


const TagManager = () => {
    const [tag, setTag] = useState([]);
    const [searchText, setSearchText] = useState("");
    const searchInput = useRef(null);
    const [searchedColumn, setSearchedColumn] = useState("");
    const [isAddModalVisible, setIsAddModalVisible] = useState(false);


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
            title: "Nome da tag",
            dataIndex: "tag",
            key: "tag",
            ...getColumnSearchProps("tag"),
        },
        {
            title: "Ação",
            key: "action",
            width: "0",
            render: (_, record) => (
                <Space>
                    <Button type="dashed" icon={< EyeOutlined />} />
                    <Button type="dashed" icon={< EditOutlined />} />
                    <Button type="dashed" icon={< DeleteOutlined />} />
                </Space>
            )
        }
    ];

    return (
        <>
            <div style={{ marginBottom: "20px", textAlign: "right" }}>
                <Button icon={<IoAddCircleOutline />} size="large" onClick={() => { setIsAddModalVisible(true) }}>Adicionar nova categoria</Button>
            </div>
            <Table columns={columns} dataSource={tag} />
        </>
    );
}

export default TagManager;