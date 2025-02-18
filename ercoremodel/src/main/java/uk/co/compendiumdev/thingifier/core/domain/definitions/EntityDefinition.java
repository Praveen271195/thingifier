package uk.co.compendiumdev.thingifier.core.domain.definitions;

import uk.co.compendiumdev.thingifier.core.domain.definitions.field.definition.Field;
import uk.co.compendiumdev.thingifier.core.domain.definitions.field.definition.FieldType;
import uk.co.compendiumdev.thingifier.core.domain.definitions.field.instance.FieldValue;
import uk.co.compendiumdev.thingifier.core.domain.definitions.relationship.RelationshipVectorDefinition;
import uk.co.compendiumdev.thingifier.core.domain.instances.InstanceFields;

import java.util.*;



public class EntityDefinition {
    private String name;
    private String plural;

    private DefinedFields fields;
    private DefinedRelationships definedRelationships;

    public EntityDefinition(String name, String plural) {
        this.name = name;
        this.plural = plural;
        definedRelationships = new DefinedRelationships();
        fields = new DefinedFields();

        // todo: support overriding this name to allow model to use guid as a user managed field
        // e.g. model.internalUniqueIDFieldName("bob")
        addField(Field.is("guid", FieldType.GUID));
    }
    public String toString() {
        StringBuilder output = new StringBuilder();


        output.append(fields.toString());

        return output.toString();
    }


    public String getName() {
        return name;
    }


    public String getPlural() {
        return plural;
    }


    public void addField(Field aField) {
        fields.addField(aField);
    }

    public List<String> getFieldNames() {
        return fields.getFieldNames();
    }

    public boolean hasFieldNameDefined(String fieldName) {
        return fields.hasFieldNameDefined(fieldName);
    }

    public EntityDefinition addFields(Field... theseFields) {
        fields.addFields(theseFields);
        return this;
    }

    public Field getField(String fieldName) {
        return fields.getField(fieldName);
    }

    public List<Field> getFieldsOfType(final FieldType... types) {
        return  fields.getFieldsOfType(types);
    }

    public List<String> getFieldNamesOfType(final FieldType... types) {
        return  fields.getFieldNamesOfType(types);
    }

    public DefinedRelationships related(){
        return definedRelationships;
    }

    public RelationshipVectorDefinition getNamedRelationshipTo(final String relationshipName,
                                                               final EntityDefinition entity) {

        List<RelationshipVectorDefinition> relationshipsWithThisName =
                definedRelationships.getRelationships(relationshipName);

        for (RelationshipVectorDefinition relationship : relationshipsWithThisName) {
            if (relationship.getTo() == entity) {
                return relationship;
            }
        }

        // there is no relationship with this name between the things we want
        return null;
    }

    public InstanceFields instantiateFields() {
        return new InstanceFields(fields);
    }

    /*
        given a list of field values,
        if any of those match an id field
        then set our 'next id' for that field to above
        the value provided
     */
    public void setNextIdsToAccomodate(final List<FieldValue> fieldValues) {
        // todo: process nested objects - currently assume these are not ids, but they might be
        for(FieldValue fieldNameValue : fieldValues){
            final Field field = fields.getField(fieldNameValue.getName());
            if(field!=null && field.getType()== FieldType.ID) {
                field.ensureNextIdAbove(fieldNameValue.asString());
            }
        }

    }
}
