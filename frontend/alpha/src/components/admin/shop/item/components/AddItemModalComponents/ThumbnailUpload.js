import { Upload, Image } from "antd";
import { useState } from "react";
import { InboxOutlined } from "@ant-design/icons";


const ThumbnailUpload = ( {addItemForm} ) => {
    const [thumbnail, setThumbnail] = useState();
    
    
    return (
        <Upload.Dragger
            name="files"
            maxCount={1}
            showUploadList={false}
            beforeUpload={(file) => {
                const reader = new FileReader();
                reader.onload = (e) => {
                    setThumbnail(e.target.result);
                    addItemForm.setFieldsValue({ thumbnail: e.target.result });
                };
                reader.readAsDataURL(file);
                return false;
            }}
            style={{
                width: 300,
                height: 300,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
            }}>
            {
                thumbnail ?
                    (
                        <Image
                            src={thumbnail}
                            alt="Thumbnail"
                            style={{
                                maxWidth: "100%",
                                maxHeight: "100%",
                                objectFit: "contain",
                            }}
                        />
                    ) :
                    (
                        <>
                            <p className="ant-upload-drag-icon">
                                <InboxOutlined />
                            </p>
                            <p className="ant-upload-text">Clique aqui ou arraste uma foto</p>
                            <p className="ant-upload-hint">Suporte apenas para um único upload.</p>
                        </>
                    )
            }

        </Upload.Dragger>
    )

}


export default ThumbnailUpload;