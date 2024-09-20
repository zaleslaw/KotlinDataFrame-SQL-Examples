package customdb

import org.jetbrains.kotlinx.dataframe.io.db.DbType
import org.jetbrains.kotlinx.dataframe.io.db.DbTypeProvider

class MyDbTypeProvider : DbTypeProvider {
    override fun getDbType(): DbType? {
        return HSQLDB
    }
}