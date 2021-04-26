 package pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.it.generated

 import com.querydsl.core.types.Path
 import com.querydsl.sql.PrimaryKey
 import pl.exsio.querydsl.entityql.QColumnMetadataFactory
 import pl.exsio.querydsl.entityql.QPathConfig
 import pl.exsio.querydsl.entityql.QPathFactory
 import pl.exsio.querydsl.entityql.QStaticModel
 import com.querydsl.sql.ForeignKey
 import pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.it.Group
 import com.querydsl.core.types.dsl.NumberPath
 import com.querydsl.core.types.dsl.StringPath
 import pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.it.GroupAdmin
 import javax.annotation.Generated
 import java.util.Arrays
 import groovy.transform.CompileStatic
 
 /**
 *
 * This class was generated by EntityQL (https://github.com/eXsio/querydsl-entityql).
 * It is not recommended to make any changes to this class.
 * Any manual changes will be lost upon the next class generation.
 *
 */
 @CompileStatic
 @Generated("pl.exsio.querydsl.entityql.QExporter")
 public final class QGroup extends QStaticModel<Group> {

     private static final long serialVersionUID = 1844673376

     public static final QGroup INSTANCE = new QGroup()

     public static final QGroup qGroup = INSTANCE

     public final NumberPath<Long> id

     public final StringPath name

     public final NumberPath<Long> adminId

     public final ForeignKey<GroupAdmin> admin

     public final PrimaryKey<Group> _primaryKey

     public QGroup() {
         this("GROUPS")
     }
     @SuppressWarnings(value = "unchecked")
     public QGroup(String variable) {
         super(Group.class, variable, "", "GROUPS")
         id: {
             QPathConfig config = new QPathConfig(Long.class, Long.class, "GROUP_ID", true, 1, -5)
             this.id = QPathFactory.<NumberPath<Long>>create(this, config)
             addMetadata(this.id, QColumnMetadataFactory.create(config))
             this.columnsMap.put("id", this.id)
         }

         name: {
             QPathConfig config = new QPathConfig(String.class, String.class, "NAME", true, 2, 12)
             this.name = QPathFactory.<StringPath>create(this, config)
             addMetadata(this.name, QColumnMetadataFactory.create(config))
             this.columnsMap.put("name", this.name)
         }

         adminId: {
             QPathConfig config = new QPathConfig(Long.class, Long.class, "GA_ID", true, 3, -5)
             this.adminId = QPathFactory.<NumberPath<Long>>create(this, config)
             addMetadata(this.adminId, QColumnMetadataFactory.create(config))
             this.columnsMap.put("adminId", this.adminId)
         }

         admin: {
             this.admin = this.<GroupAdmin>createForeignKey(this.adminId, "GA_ID")
             this.joinColumnsMap.put("admin", this.admin)
         }

         _primaryKey: {
             this.primaryKeyColumns = Arrays.<Path<?>>asList(this.id)
             Path[] pkArray = (Path[]) primaryKeyColumns.<Path>toArray(new Path[0])
             this._primaryKey = this.<Group>createPrimaryKey(pkArray)
         }

     }
 } 