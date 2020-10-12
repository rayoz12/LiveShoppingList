import 'package:shoppingListAPI/api.dart';

extension itemEquality on Item {
  bool equal(Object other) =>
      identical(this, other) ||
      other is Item && runtimeType == other.runtimeType && this.toString() == other.toString();
}
