package com.readutf.commands.core.utils;

import com.readutf.commands.core.Command;
import com.readutf.commands.core.input.CommandInput;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommandMap {

    private final Node topLevelNode = new Node(null);

    public void addCommand(String path, @NotNull Command command) {
        String[] parts = path.split(" ");
        Node current = topLevelNode;

//        for (int i = 0; i < parts.length; i++) {
//            String part = parts[i];
//
//            if (i == parts.length - 1) {
//                current.addChild(part, new Node(part, command));
//                return;
//            }
//            Node child = current.getChild(part);
//            if (child == null) {
//                child = new Node(part);
//                current.addChild(part, child);
//            }
//            current = child;
//        }

        for (String part : parts) {
            Node child = current.getChild(part);
            if (child == null) {
                child = new Node(part);
                current.addChild(part, child);
            }
            current = child;
        }
        current.setValue(command);

    }

    public @NotNull List<Command> getCommand(CommandInput commandInput) {
        String[] parts = commandInput.getCommandParts();

        System.out.println(Arrays.toString(parts));

        Node current = topLevelNode;
        for (String part : parts) {
            Node child = current.getChild(part);
            if (child == null) {
                return current == topLevelNode ? Collections.emptyList() : Collections.singletonList(current.getValue());
            }
            current = child;
        }

        return recursiveGetCommands(current);
    }

    public Node getTree() {
        return topLevelNode;
    }

    private List<Command> recursiveGetCommands(Node node) {
        List<Command> commands = new ArrayList<>();
        if (node.getValue() != null) {
            commands.add(node.getValue());
        }

        for (Node child : node.getChildren().values()) {
            commands.addAll(recursiveGetCommands(child));
        }

        return commands;
    }

    @Override
    public String toString() {
        return "CommandMap{" +
                "topLevelNode=" + topLevelNode +
                '}';
    }

    @Getter
    public class Node {

        private final String commandPart;
        private Map<String, Node> children = new HashMap<>();
        private @Setter @Nullable Command value;

        public Node(String commandPart, @NotNull Command value) {
            this.commandPart = commandPart;
            this.value = value;
        }

        public Node(String commandPart) {
            this.commandPart = commandPart;
        }

        public void addChild(String key, Node node) {
            children.put(key, node);
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

        public void setValue(@NotNull Command value) {
            this.value = value;
        }

        public @Nullable Node getChild(String key) {
            return children.get(key);
        }

        public List<Node> getAllChildren() {
            if (isLeaf()) return Collections.singletonList(this);
            List<Node> children = new ArrayList<>();
            for (Node child : this.children.values()) {
                children.addAll(child.getAllChildren());
            }
            return children;
        }

        public int getDepth() {
            if(value != null) {
                return 0;
            }
            return 1 + children.values().stream().mapToInt(Node::getDepth).max().orElse(0);
        }
    }

}
