class AVLNode {
    constructor(value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}

class AVLTree {
    constructor() {
        this.root = null;
    }

    height(node) {
        return node ? node.height : 0;
    }

    getBalanceFactor(node) {
        return node ? this.height(node.left) - this.height(node.right) : 0;
    }

    rightRotate(y) {
        let x = y.left;
        let T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = Math.max(this.height(y.left), this.height(y.right)) + 1;
        x.height = Math.max(this.height(x.left), this.height(x.right)) + 1;
        return x;
    }

    leftRotate(x) {
        let y = x.right;
        let T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = Math.max(this.height(x.left), this.height(x.right)) + 1;
        y.height = Math.max(this.height(y.left), this.height(y.right)) + 1;
        return y;
    }

    insert(node, value) {
        if (!node) return new AVLNode(value);
        if (value < node.value) node.left = this.insert(node.left, value);
        else if (value > node.value) node.right = this.insert(node.right, value);
        else return node;

        node.height = 1 + Math.max(this.height(node.left), this.height(node.right));
        let balance = this.getBalanceFactor(node);

        if (balance > 1 && value < node.left.value) return this.rightRotate(node);
        if (balance < -1 && value > node.right.value) return this.leftRotate(node);
        if (balance > 1 && value > node.left.value) {
            node.left = this.leftRotate(node.left);
            return this.rightRotate(node);
        }
        if (balance < -1 && value < node.right.value) {
            node.right = this.rightRotate(node.right);
            return this.leftRotate(node);
        }

        return node;
    }

    minValueNode(node) {
        let current = node;
        while (current.left) {
            current = current.left;
        }
        return current;
    }

    delete(node, value) {
        if (!node) return node;

        if (value < node.value) node.left = this.delete(node.left, value);
        else if (value > node.value) node.right = this.delete(node.right, value);
        else {
            if (!node.left || !node.right) {
                node = node.left ? node.left : node.right;
            } else {
                let temp = this.minValueNode(node.right);
                node.value = temp.value;
                node.right = this.delete(node.right, temp.value);
            }
        }

        if (!node) return node;

        node.height = 1 + Math.max(this.height(node.left), this.height(node.right));
        let balance = this.getBalanceFactor(node);

        if (balance > 1 && this.getBalanceFactor(node.left) >= 0) return this.rightRotate(node);
        if (balance > 1 && this.getBalanceFactor(node.left) < 0) {
            node.left = this.leftRotate(node.left);
            return this.rightRotate(node);
        }
        if (balance < -1 && this.getBalanceFactor(node.right) <= 0) return this.leftRotate(node);
        if (balance < -1 && this.getBalanceFactor(node.right) > 0) {
            node.right = this.rightRotate(node.right);
            return this.leftRotate(node);
        }

        return node;
    }

    insertValue(value) {
        this.root = this.insert(this.root, value);
        drawTree();
    }

    deleteValue(value) {
        this.root = this.delete(this.root, value);
        drawTree();
    }
}

const avlTree = new AVLTree();

function insertNode() {
    const value = parseInt(document.getElementById('nodeValue').value);
    if (!isNaN(value)) {
        avlTree.insertValue(value);
    }
}

function deleteNode() {
    const value = parseInt(document.getElementById('nodeValue').value);
    if (!isNaN(value)) {
        avlTree.deleteValue(value);
    }
}

function drawTree() {
    const svg = document.getElementById("tree-container");
    svg.innerHTML = "";
    drawNode(svg, avlTree.root, window.innerWidth / 2, 50, window.innerWidth / 4);
}

function drawNode(svg, node, x, y, offset) {
    if (!node) return;

    if (node.left) {
        drawLine(svg, x, y, x - offset, y + 80);
        drawNode(svg, node.left, x - offset, y + 80, offset / 2);
    }
    if (node.right) {
        drawLine(svg, x, y, x + offset, y + 80);
        drawNode(svg, node.right, x + offset, y + 80, offset / 2);
    }

    drawCircle(svg, x, y, node.value);
}

function drawCircle(svg, x, y, value) {
    let circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    circle.setAttribute("cx", x);
    circle.setAttribute("cy", y);
    circle.setAttribute("r", 20);
    svg.appendChild(circle);

    let text = document.createElementNS("http://www.w3.org/2000/svg", "text");
    text.setAttribute("x", x);
    text.setAttribute("y", y + 5);
    text.setAttribute("text-anchor", "middle");
    text.textContent = value;
    svg.appendChild(text);
}

function drawLine(svg, x1, y1, x2, y2) {
    let line = document.createElementNS("http://www.w3.org/2000/svg", "line");
    line.setAttribute("x1", x1);
    line.setAttribute("y1", y1);
    line.setAttribute("x2", x2);
    line.setAttribute("y2", y2);
    svg.appendChild(line);
}
