package com.example.demo;

import com.example.demo.entities.TestPosition;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.ebean.Ebean;
import io.ebean.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.stream.Stream;

@Slf4j
@Route("")
public class BaseLayout extends Div {

    private final TextField latitudeField = new TextField("Latitude");
    private final TextField longitudeField = new TextField("Longitude");
    private final VerticalLayout layout = new VerticalLayout();
    private final Label label = new Label("");

    public BaseLayout() {
        add(layout);
        layout.add(latitudeField);
        layout.add(longitudeField);

        layout.add(new Button("Generate some rows", event -> generateSomeRows()));
        layout.add(new Button("Count with distance", event -> countDistanceQuery()));
        layout.add(new Button("List with distance", event -> fetchWithDistanceQuery()));
        layout.add(new Button("List without distance", event -> fetchWithoutDistanceQuery()));
        layout.add(new Label("Result:"), label);
    }

    private void fetchWithoutDistanceQuery() {
        label.setText(
            Ebean
                .createQuery(TestPosition.class)
                .findList()
                .toString()
        );
    }

    private void fetchWithDistanceQuery() {
        label.setText("" + getQuery().findList().toString());
    }

    private Query<TestPosition> getQuery() {
        return Ebean
            .createQuery(TestPosition.class)
            .select("(st_distance(position, point(:lat, :lng)) * 1000 as distance)::Double")
            .setParameter("lat", Double.valueOf(latitudeField.getValue()))
            .setParameter("lng", Double.valueOf(longitudeField.getValue()));
    }

    private void countDistanceQuery() {
        label.setText("" + getQuery().findCount());
    }

    private void generateSomeRows() {
        Stream.of(
            Pair.of(47.4738016, 19.0690168),
            Pair.of(47.4783896, 19.0672839),
            Pair.of(47.4902998, 19.0604549),
            Pair.of(47.487744, 19.0560161)
        ).forEach(position -> {
                TestPosition testPosition = new TestPosition();
                Ebean.insert(testPosition);
                Ebean
                    .update(TestPosition.class)
                    .where().eq("id", testPosition.getId())
                    .asUpdate()
                    .setRaw("position = Point(?, ?)", position.getLeft(), position.getRight())
                    .update();
            }
        );
    }
}
