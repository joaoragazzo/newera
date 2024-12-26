export function NotificationContent( {title, description} ) {
    return (
        <div>
            <div style={{ fontWeight: "bold", color: "#3c89e8" }}>
                {title}
            </div>
            <div style={{ color: "#ffffff" }}>
                {description}
            </div>
        </div>
    )
}