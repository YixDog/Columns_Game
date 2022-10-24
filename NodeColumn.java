public class NodeColumn {
    private String columnName;
    private NodeS right;
    private NodeColumn down;

    public NodeColumn(String columnName) {
        this.columnName = columnName;
        this.right = null;
        this.down = null;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public NodeS getRight() {
        return right;
    }

    public void setRight(NodeS right) {
        this.right = right;
    }

    public NodeColumn getDown() {
        return down;
    }

    public void setDown(NodeColumn down) {
        this.down = down;
    }
}
