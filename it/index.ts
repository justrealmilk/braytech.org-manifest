import Braytech from '@Data/manifest/it/Braytech.json';
import BraytechMaps from '@Data/manifest/it/BraytechMaps.json';
import BraytechMapsTypes from '@Data/manifest/it/BraytechMapsTypes.json';
import DestinyActivityDefinition from '@Data/manifest/it/DestinyActivityDefinition.json';
import DestinyActivityModeDefinition from '@Data/manifest/it/DestinyActivityModeDefinition.json';
import DestinyActivityModifierDefinition from '@Data/manifest/it/DestinyActivityModifierDefinition.json';
import DestinyChecklistDefinition from '@Data/manifest/es/DestinyChecklistDefinition.json';
import DestinyCollectibleDefinition from '@Data/manifest/it/DestinyCollectibleDefinition.json';
import DestinyDamageTypeDefinition from '@Data/manifest/it/DestinyDamageTypeDefinition.json';
import DestinyDestinationDefinition from '@Data/manifest/it/DestinyDestinationDefinition.json';
import DestinyEnemyRaceDefinition from '@Data/manifest/it/DestinyEnemyRaceDefinition.json';
import DestinyHistoricalStatsDefinition from '@Data/manifest/it/DestinyHistoricalStatsDefinition.json';
import DestinyInventoryBucketDefinition from '@Data/manifest/it/DestinyInventoryBucketDefinition.json';
import DestinyInventoryItemDefinition from '@Data/manifest/it/DestinyInventoryItemDefinition.json';
import DestinyMilestoneDefinition from '@Data/manifest/it/DestinyMilestoneDefinition.json';
import DestinyObjectiveDefinition from '@Data/manifest/it/DestinyObjectiveDefinition.json';
import DestinyPhaseDefinition from '@Data/manifest/it/DestinyPhaseDefinition.json';
import DestinyPresentationNodeDefinition from '@Data/manifest/it/DestinyPresentationNodeDefinition.json';
import DestinyRecordDefinition from '@Data/manifest/it/DestinyRecordDefinition.json';
import DestinySeasonDefinition from '@Data/manifest/it/DestinySeasonDefinition.json';
import DestinyTraitDefinition from '@Data/manifest/it/DestinyTraitDefinition.json';

import DestinyInventoryItemDefinitionColloquial from '@Data/manifest/it/DestinyInventoryItemDefinitionColloquial.json';

const it = {
  definitions: {
    Braytech,
    BraytechMaps,
    BraytechMapsTypes,
    DestinyActivityDefinition,
    DestinyActivityModeDefinition,
    DestinyActivityModifierDefinition,
    DestinyChecklistDefinition,
    DestinyCollectibleDefinition,
    DestinyDamageTypeDefinition,
    DestinyDestinationDefinition,
    DestinyEnemyRaceDefinition,
    DestinyHistoricalStatsDefinition,
    DestinyInventoryBucketDefinition,
    DestinyInventoryItemDefinition,
    DestinyMilestoneDefinition,
    DestinyObjectiveDefinition,
    DestinyPhaseDefinition,
    DestinyPresentationNodeDefinition,
    DestinyRecordDefinition,
    DestinySeasonDefinition,
    DestinyTraitDefinition,
  },
  optional: {
    colloquialDefinitions: {
      DestinyInventoryItemDefinition: DestinyInventoryItemDefinitionColloquial,
    },
  },
};

export default it;
